import * as React from 'react';

const UploadProgress = ({ progress }) => (
  <div className='UploadProgress'>
    Uploaded: { progress } %
  </div>
);

export default UploadProgress;