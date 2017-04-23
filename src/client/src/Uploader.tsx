import * as React from 'react';
import axios from 'axios';
import * as update from 'immutability-helper';
import UploadProgress from './UploadProgress';

interface IUploaderProps {

}

interface IUploaderState {
  file: any,
  progress: number
}

class Uploader extends React.Component<IUploaderProps, IUploaderState> {
  constructor(props) {
    super(props);
    this.state = { file: null, progress: 0 };

    this.onFileInput = this.onFileInput.bind(this);
    this.onFileUpload = this.onFileUpload.bind(this);
  }

  onFileUpload(event) {
    const { state } = this;
    const self = this;

    axios.patch(`http://localhost:8080/upload/${state.file.name.slice(0, state.file.name.length-4)}`, state.file, {
      headers: {
        'content-type': 'text/plain',
        fileName: state.file.name.slice(0, state.file.name.length-4),
        partNumber: 1,
        uploadLength: state.file.size,
        userName: 'cjvirtucio'
      },
      onUploadProgress(ev) {
        const progress = Math.floor((ev.loaded / state.file.size) * 100);
        const newState = update(state, { progress: { $set: progress }});

        self.setState(newState);
      }
    }).catch(err => console.log(err));
  }

  onFileInput(event) {
    const { state } = this;
    const reader = new FileReader();
    const file = event.target.files[0];
    const newState = update(state, { file: { $set: file }});
    const onLoadEnd = () => this.setState(newState);

    reader.onloadend = onLoadEnd;
    reader.readAsDataURL(file);
  }

  render() {
    return (
      <div className='Uploader'>
        <label htmlFor='fileUploader'>File Uploader</label>
        <input id='fileUploader' type='file' onChange={ this.onFileInput } /><br/>
        <button type='button' onClick={ this.onFileUpload }>Upload</button><br/>
        <UploadProgress progress={ this.state.progress } />
      </div>
    )
  }
}

export default Uploader