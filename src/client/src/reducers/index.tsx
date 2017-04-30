/* 
  Reducers are handlers that deal with the messages(actions).

  You can merge multiple reducers using the combineReducers function.
*/

import * as update from 'immutability-helper';

const updateRecord = (state, progressParams) => {
  const { partNumber, progress, speed } = progressParams;
  const progressData =  update(state.progressData, { [partNumber]: { $set: { partNumber, progress, speed } } });
  
  return update(state, { progressData: { $set: progressData } });
}

export const file = (state = { progressData: {} }, action) => {
  switch (action.type) {
    case 'ADD_FILE':
      return {
        ...state,
        parts: action.parts
      };
    case 'UPLOAD_PART':
      return {
        ...state,
        part: action.part
      };
    case 'UPDATE_PROGRESS':
      return updateRecord(state, action.progressParams);
    default:
      return state;
  }
};