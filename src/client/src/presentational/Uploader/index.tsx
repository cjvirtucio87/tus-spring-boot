import * as React from 'react';
import * as update from 'immutability-helper';

import axios from 'axios';

const Uploader = ({ onAddFile, onUploadFile, parts }) => (
  <div className='Uploader'>
      <label htmlFor='fileUploader'>File Uploader</label>
      <input id='fileUploader' type='file' onChange={ onAddFile } /><br/>
      <button type='button' onClick={ onUploadFile(parts) }>Upload</button><br/>
  </div>
);

export default Uploader