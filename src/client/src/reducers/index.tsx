/* 
  Reducers are handlers that deal with the messages(actions).

  You can merge multiplate reducers using the combineReducers function.
*/

export const file = (state = {}, action) => {
  switch (action.type) {
    case 'ADD_FILE':
      return {
        ...state,
        file: action.file
      };
    case 'UPLOAD_FILE':
      return {
        ...state,
        file: action.file
      };
    case 'UPDATE_PROGRESS':
      return {
        ...state,
        progressParams: action.progressParams
      };
    default:
      return state;
  }
};