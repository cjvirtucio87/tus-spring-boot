/* 
  Actions are messages containing with payload for updating state.

  Note the syntax regarding the payload: { file } and { progressParams } are synctatic sugar for { file: file } and { progressParams: progressParams } (ES6 feature).
*/

export const addFile = (parts) => ({
  type: 'ADD_FILE',
  parts
});

export const uploadPart = (part) => ({
  type: 'UPLOAD_PART',
  part
});

export const updateProgress = (progressParams) => ({
  type: 'UPDATE_PROGRESS',
  progressParams
});

export const finishUpload = () => ({
  type: 'UPLOAD_DONE'
})

export const toggleChunkMode = () => ({
  type: 'TOGGLE_CHUNK_MODE'
});