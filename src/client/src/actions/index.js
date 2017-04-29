"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.addFile = (parts) => ({
    type: 'ADD_FILE',
    parts
});
exports.uploadPart = (part) => ({
    type: 'UPLOAD_PART',
    part
});
exports.updateProgress = (progressParams) => ({
    type: 'UPDATE_PROGRESS',
    progressParams
});
//# sourceMappingURL=index.js.map