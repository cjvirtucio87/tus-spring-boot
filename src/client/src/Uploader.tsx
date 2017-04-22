import * as React from 'react';
import * as axios from 'axios';

class Uploader extends React.Component<{}, {}> {
  constructor(props) {
    super(props);

    this.onFileInput = this.onFileInput.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    console.log('firing');
  }

  onFileInput(event) {
    const { state } = this;
    const reader = new FileReader();
    const file = event.target.files[0];
    const newState = { ...state, ...file };
    const onLoadEnd = () => this.setState(newState);

    reader.onloadend = onLoadEnd;
    reader.readAsDataURL(file);
  }

  render() {
    return (
      <div className='Uploader'>
        <form>
          <label htmlFor='fileUploader'>File Uploader</label>
          <input id='fileUploader' type='file' onChange={ this.onFileInput } /><br/>
          <button type='submit'>Upload</button>
        </form>
      </div>
    )
  }
}

export default Uploader