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
exports.updateProgress = function (progress) { return ({
    type: 'UPDATE_PROGRESS',
    progress: progress
}); };
//# sourceMappingURL=index.js.map