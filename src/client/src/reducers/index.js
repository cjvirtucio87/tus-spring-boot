"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.file = (state = {}, action) => {
    switch (action.type) {
        case 'ADD_FILE':
            return Object.assign({}, state, { parts: action.parts });
        case 'UPLOAD_PART':
            return Object.assign({}, state, { part: action.part });
        case 'UPDATE_PROGRESS':
            return Object.assign({}, state, { progressParams: action.progressParams });
        default:
            return state;
    }
};
//# sourceMappingURL=index.js.map