"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
const Uploader = ({ onAddFile, onUploadFile, parts }) => (React.createElement("div", { className: 'Uploader' },
    React.createElement("label", { htmlFor: 'fileUploader' }, "File Uploader"),
    React.createElement("input", { id: 'fileUploader', type: 'file', onChange: onAddFile }),
    React.createElement("br", null),
    React.createElement("button", { type: 'button', onClick: onUploadFile(parts) }, "Upload"),
    React.createElement("br", null)));
exports.default = Uploader;
//# sourceMappingURL=index.js.map