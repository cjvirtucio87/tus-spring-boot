import axios from 'axios';
import * as Rx from 'rxjs';
import * as moment from 'moment';

import { uploadPart, updateProgress } from '../actions';

// Math
const computeProgress = (loaded, fileSize) => Math.floor((loaded / fileSize) * 100);
const computeElapsedTime = (unit) => (startTime) => moment().diff(startTime, unit) || 1;
const computeElapsedSeconds = computeElapsedTime('seconds');
const computeSpeed = (loaded, startTime) => Math.floor(loaded / computeElapsedSeconds(startTime));

const upload = dispatch => startTime => part => {
  return axios.patch(`http://localhost:8080/upload/${part.name}/${part.number}`, part.file, {
    headers: {
      'content-type': 'text/plain',
      fileName: part.name,
      partNumber: part.number,
      uploadLength: part.file.size,
      userName: 'cjvirtucio'
    },
    onUploadProgress(ev) {
      const progress = computeProgress(ev.loaded, part.file.size);
      const speed = computeSpeed(ev.loaded, startTime);

      dispatch(updateProgress({ part, progress, speed }));
      if (progress === 100) dispatch(uploadPart(part));
    }
  }).catch(err => console.log(err));
}

const uploadFactory = dispatch => startTime => parts => {
  // Rx.Observable.of(parts);
}