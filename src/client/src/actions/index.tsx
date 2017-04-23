/* Actions are messages containing with payload for updating state. */

export const addFile = (file) => ({
  type: 'ADD_FILE',
  file
});

export const uploadFile = (file) => ({
  type: 'UPLOAD_FILE',
  file
});

export const updateProgress = (progress) => ({
  type: 'UPDATE_PROGRESS',
  progress
});