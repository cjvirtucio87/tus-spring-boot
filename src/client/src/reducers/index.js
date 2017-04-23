"use strict";
var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
exports.__esModule = true;
exports.file = function (state, action) {
    if (state === void 0) { state = {}; }
    switch (action.type) {
        case 'ADD_FILE':
            return __assign({}, state, { file: action.file });
        case 'UPLOAD_FILE':
            return __assign({}, state, { file: action.file });
        case 'UPDATE_PROGRESS':
            return {
                progress: action.progress
            };
        default:
            return state;
    }
};
//# sourceMappingURL=index.js.map