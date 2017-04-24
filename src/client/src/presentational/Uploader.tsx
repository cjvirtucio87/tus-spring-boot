import * as React from 'react';
import * as update from 'immutability-helper';

import axios from 'axios';

const Uploader = ({ onAddFile, onUploadFile, file }) => {
  return (
    <div className='Uploader'>
        <p>File Name: { file ? file.name : "" }</p>

        <label htmlFor='fileUploader'>File Uploader</label>
        <input id='fileUploader' type='file' onChange={ onAddFile } /><br/>
        <button type='button' onClick={ onUploadFile.bind(null, file) }>Upload</button><br/>
    </div>
  )
}

export default Uploader