import * as React from 'react';
import * as update from 'immutability-helper';

import axios from 'axios';

const Part = ({ part }) => <p>File Part: { part.file.name }</p>

const Uploader = ({ onAddFile, parts }) => {
  const partNodes = parts ? parts.map(part => <Part part={ part }/>) : <p>No uploads yet</p>;

  return (
    <div className='Uploader'>
        { partNodes }

        <label htmlFor='fileUploader'>File Uploader</label>
        <input id='fileUploader' type='file' onChange={ onAddFile } /><br/>
        {/*<button type='button' onClick={ onUploadFile.bind(null, file) }>Upload</button><br/>*/}
    </div>
  )
}

export default Uploader