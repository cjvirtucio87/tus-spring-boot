import * as React from 'react';

const UploadProgress = ({ progressParams }) => {
  const { progress, speed } = progressParams ? progressParams : { progress: 0, speed: 0 };

  return (
    <div className='UploadProgress'>
      <p>Uploaded: { progress } %</p>
      <p>Upload Speed: { speed } bytes/sec</p>
    </div>
  )
};

export default UploadProgress;