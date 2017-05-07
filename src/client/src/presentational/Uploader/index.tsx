import * as React from 'react';
import * as update from 'immutability-helper';

import axios from 'axios';

const Uploader = ({ onAddFile, onUploadFile, onChunkToggle, parts, chunked }) => (
  <div className='Uploader'>
      <label htmlFor='fileUploader'>File Uploader</label>
      <h3>{ chunked ? "CHUNKED MODE" : "SINGLE UPLOAD MODE" }</h3>
      <input id='fileUploader' type='file' onChange={ onAddFile(chunked) } /><br/>
      <button type='button' onClick={ onChunkToggle }>Toggle Chunk Upload</button><br/>
      <button type='button' onClick={ onUploadFile(parts) }>Upload</button><br/>
  </div>
);

export default Uploader