import * as React from 'react';
import * as axios from 'axios';
import * as update from 'immutability-helper';

interface IUploaderProps {

}

interface IUploaderState {
  file: any
}

class Uploader extends React.Component<IUploaderProps, IUploaderState> {
  constructor(props) {
    super(props);
    this.state = { file: null };

    this.onFileInput = this.onFileInput.bind(this);
  }

  componentWillUpdate(nextProps, nextState) {
    console.log('Component Will Update');
    console.log(nextState);
  }

  onFileUpload(event) {
    console.log(`Uploading file: `);
    // console.log(this.state.file);
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
        <button type='button' onClick={ this.onFileUpload }>Upload</button>
      </div>
    )
  }
}

export default Uploader