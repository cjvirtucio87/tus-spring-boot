/* 
  The most common ancestor will be a 'container' (stateful) component that encapsulates behavior dealing with state.
  All expensive operations will take place in the container component, and will communicate with the store in the process.

  Use the mapStateToProps and mapDispatchToProps to connect this component to the store.
*/

import * as React from 'react';
import { connect } from 'react-redux';

import { addFile, uploadFile, updateProgress } from '../actions';

import presentational from '../presentational/';

import axios from 'axios';
import * as moment from 'moment';

const { Uploader, UploadProgress } = presentational;

// Math
const computeProgress = (loaded, fileSize) => Math.floor((loaded / fileSize) * 100);
const computeSpeed = (loaded, startTime) => Math.floor(loaded / parseInt(moment().subtract(startTime).format('SS')));

// Dispatchers
const onUploadFile = dispatch => (file) => {
  const startTime = parseInt(moment().format('SS'));

  // TODO: generalized fileName getter
  axios.patch(`http://localhost:8080/upload/${file.name.slice(0, file.name.length-4)}`, file, {
    headers: {
      'content-type': 'text/plain',
      fileName: file.name.slice(0, file.name.length-4),
      partNumber: 1,
      uploadLength: file.size,
      userName: 'cjvirtucio'
    },
    onUploadProgress(ev) {
      const progress = computeProgress(ev.loaded, file.size);
      const speed = computeSpeed(ev.loaded, startTime);

      dispatch(updateProgress({ progress, speed }));
      if (progress === 100) dispatch(uploadFile(file));
    }
  }).catch(err => console.log(err));
}

const onAddFile = dispatch => (event) => {
  const reader = new FileReader();
  const file = event.target.files[0];
  reader.onloadend = () => dispatch(addFile(file));
  reader.readAsDataURL(file);
}

// Store Connectors
const mapStateToProps = (state) => ({
  file: state.file,
  progressParams: state.progressParams
});

const mapDispatchToProps = (dispatch) => ({
  onAddFile: onAddFile(dispatch),
  onUploadFile: onUploadFile(dispatch)
});

const Dashboard = ({ onAddFile, onUploadFile, file, progressParams }) => (
  <div className='Dashboard'>
    <Uploader
      onAddFile={ onAddFile }
      onUploadFile={ onUploadFile }
      file={ file }
    />
    <UploadProgress
      progressParams={ progressParams }
    />
  </div>
);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Dashboard);