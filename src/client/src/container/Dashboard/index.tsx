/* 
  The most common ancestor will be a 'container' (stateful) component that encapsulates behavior dealing with state.
  All expensive operations will take place in the container component, and will communicate with the store in the process.

  Use the mapStateToProps and mapDispatchToProps to connect this component to the store.
*/

import * as React from 'react';
import { connect } from 'react-redux';

import { PART_SIZE, BASE_URI, FILENAME_PATTERN } from '../../constants';
import { addFile, updateProgress, finishUpload, toggleChunkMode } from '../../actions';

import presentational from '../../presentational/';

import { computeProgress, computeElapsedTime } from '../../utils/local-math';
import axios from 'axios';
import * as moment from 'moment';
import * as Rx from 'rxjs';

import './style.css';

const { Uploader, UploadProgress } = presentational;

// Math
const computeElapsedSeconds = computeElapsedTime('seconds');
const computeSpeed = (loaded, startTime) => Math.floor(loaded / ( computeElapsedSeconds(startTime) ));

const capAtFilesize = (value, fileSize) => value > fileSize ? fileSize : value;

const createFilePart = (file, fileName) => (
  {
    file, 
    fileName, 
    partNumber: 0, 
    uploadOffset: 0, 
    uploadLength: file.size,
    fileSize: file.size
  }
);

const createFileParts = (file, fileName, uploadOffset, uploadLength, partNumber, parts) => {
  const fileSize = file.size;
  if (uploadOffset >= file.size) return parts;

  /*
    0:2, 3:4, 5:6
    0:2, 0:1, 0:1
    2 bytes to be transferred; 1 byte to be transferred; 1 byte to be transferred;
    bytesToBeTransferred = (len - offset)
    upperBoundPart = bytesToBeTransferred
    lowerBoundPart = 0
    Therefore, we only transfer bytes when (bytesToBeTransferred - lowerBoundPart) > 0;
  */
  
  parts.push({
    file: file.slice(uploadOffset, uploadLength + 1),
    fileName,
    partNumber,
    uploadOffset: capAtFilesize(uploadOffset, fileSize),
    uploadLength: capAtFilesize(uploadLength, fileSize),
    fileSize
  });

  return createFileParts(
    file, 
    fileName, 
    capAtFilesize(uploadOffset + PART_SIZE, fileSize), 
    capAtFilesize(uploadLength + PART_SIZE, fileSize), 
    partNumber + 1, parts);
}

const onFileNotExist = dispatch => fileName => parts => () => {
  console.log(`File not found. Creating directory for file, ${fileName}`);
  axios.post(`${BASE_URI}`, null, {
    headers: {
      fileName
    }
  }).then(({ headers }) => {
    const { filedir } = headers;
    console.log(`Created directory, ${filedir}`);
    dispatch(addFile(parts));
  });
}

const onLoadEnd = dispatch => file => chunked => () => {
  const fileName = FILENAME_PATTERN.exec(file.name)[1];
  const parts = chunked ? createFileParts(file, fileName, 0, PART_SIZE, 0, []) : [createFilePart(file, fileName)];
  const partNumbers = chunked ? parts.map(part => part.partNumber) : [0];

  axios.get(`${BASE_URI}`, {
    headers: {
      fileName,
      partNumbers
    }
  })
  .then(({ data }) => {
    console.log(data);
    const message = addFile( parts.map( (p,i) => ({ ...p, loaded: data[i] }) ) );
    dispatch(message);
  })
  .catch(onFileNotExist(dispatch)(fileName)(parts));
}

const onAddFile = dispatch => chunked => event => {
  const reader = new FileReader();
  const file = event.target.files[0];
  reader.onloadend = onLoadEnd(dispatch)(file)(chunked);
  reader.readAsDataURL(file);
}

const uploadPart = dispatch => startTime => part => {
  const { partNumber, uploadOffset, uploadLength, file, fileName } = part;

  return axios.patch(`${BASE_URI}/${fileName}`, file, {
    headers: {
      'content-type': 'text/plain',
      fileName,
      partNumber,
      uploadOffset,
      uploadLength,
      userName: 'cjvirtucio'
    },
    onUploadProgress(ev) {
      const progress = computeProgress(ev.loaded, file.size);
      const speed = computeSpeed(ev.loaded, startTime);

      dispatch(updateProgress({ partNumber, progress, speed }));
    }
  })
  .then(() => "done");
};

const onPartsComplete = fileName => partNumbers => fileSize => dispatch => {
  axios.post(`${BASE_URI}/complete`, null, {
    headers: {
      fileName,
      partNumbers,
      fileSize
    }
  })
  .then(() => dispatch(finishUpload()))
  .catch(err => console.log(err));
}

const onUploadFile = dispatch => parts => event => {
  const startTime = moment();
  const { fileName, fileSize } = parts[0];
  const partNumbers = parts.map(p => p.partNumber);
  const len = parts.length;

  const source = Rx.Observable.from(parts)
    .map(uploadPart(dispatch)(startTime))
    .take(len)
    .combineAll();
  
  source.subscribe((doneParts: String[]) => {
    if (doneParts.every(p => p === "done")) {
      onPartsComplete(fileName)(partNumbers)(fileSize)(dispatch);
    }
  });
}

const onChunkToggle = dispatch => event => {
  dispatch(toggleChunkMode());
}

// Store Connectors
const mapStateToProps = state => ({
  file: state.file,
  parts: state.parts,
  uploadDone: state.uploadDone,
  chunked: state.chunked,
  progressData: state.progressData
});

const mapDispatchToProps = dispatch => ({
  onAddFile: onAddFile(dispatch),
  onUploadFile: onUploadFile(dispatch),
  onChunkToggle: onChunkToggle(dispatch)
});

const Dashboard = ({ onAddFile, onUploadFile, onChunkToggle, parts, progressData, uploadDone, chunked }) => (
  <div className='Dashboard container-fluid'>
    <section className='row align-items-center justify-content-center'>
      <div className='col-4'>
        <h3>File Uploader</h3>
        <p>Upload a file. The client slices it into chunks and will fire off a request for each one when you hit the upload button.</p>
      </div>
      <div className='col-4'>
        <Uploader
          onAddFile={ onAddFile }
          onUploadFile={ onUploadFile }
          onChunkToggle={ onChunkToggle }
          chunked={ chunked }
          parts={ parts }
        />
      </div>
    </section>
    <section className='row align-items-center justify-content-center'>
      <div className='col-4'>
        <h3>Upload Progress</h3>
        <p>This component will reveal a table showing the progress of { chunked ? "each chunk." : "the file." }</p>
        <p>{ uploadDone ? "Done!" : (parts ? "Your upload is in progress.." : "") }</p>
      </div>
      <div className='col-4'>
        <UploadProgress
          parts={ parts }
          progressData={ progressData }
        />
      </div>
    </section>
  </div>
);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Dashboard);