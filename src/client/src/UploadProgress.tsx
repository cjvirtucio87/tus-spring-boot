import * as React from 'react';

function UploadProgress(props) {
  return (
    <div className='UploadProgress'>
      Uploaded: { props.progress } %
    </div>
  )
}

export default UploadProgress;