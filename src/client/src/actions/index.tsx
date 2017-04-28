/* 
  Actions are messages containing with payload for updating state.

  Note the syntax regarding the payload: { file } and { progressParams } are synctatic sugar for { file: file } and { progressParams: progressParams } (ES6 feature).
*/

export const addFile = (file) => ({
  type: 'ADD_FILE',
  file
});

export const uploadFile = (file) => ({
  type: 'UPLOAD_FILE',
  file
});

export const updateProgress = (progressParams) => ({
  type: 'UPDATE_PROGRESS',
  progressParams
});