"use strict";
exports.__esModule = true;
exports.addFile = function (file) { return ({
    type: 'ADD_FILE',
    file: file
}); };
exports.uploadFile = function (file) { return ({
    type: 'UPLOAD_FILE',
    file: file
}); };
exports.updateProgress = function (progressParams) { return ({
    type: 'UPDATE_PROGRESS',
    progressParams: progressParams
}); };
//# sourceMappingURL=index.js.map