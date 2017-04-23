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

const { Uploader, UploadProgress } = presentational;

const mapStateToProps = (state) => ({
  file: state.file,
  progress: state.progress
});

const mapDispatchToProps = (dispatch) => ({
  onAddFile(event) {
    const reader = new FileReader();
    const file = event.target.files[0];
    reader.onloadend = () => dispatch(addFile(file));
    reader.readAsDataURL(file);
  },
  onUploadFile(file) {
    axios.patch(`http://localhost:8080/upload/${file.name.slice(0, file.name.length-4)}`, file, {
      headers: {
        'content-type': 'text/plain',
        fileName: file.name.slice(0, file.name.length-4),
        partNumber: 1,
        uploadLength: file.size,
        userName: 'cjvirtucio'
      },
      onUploadProgress(ev) {
        const progress = Math.floor((ev.loaded / file.size) * 100);
        dispatch(updateProgress(progress));
        if (progress === 100) dispatch(uploadFile(file));
      }
    }).catch(err => console.log(err));
  }
});

const Dashboard = ({ onAddFile, onUploadFile, file, progress }) => (
  <div className='Dashboard'>
    <Uploader
      onAddFile={ onAddFile }
      onUploadFile={ onUploadFile }
      file={ file }
    />
    <UploadProgress
      progress={ progress }    
    />
  </div>
);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Dashboard);