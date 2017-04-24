"use strict";
exports.__esModule = true;
var React = require("react");
var Uploader = function (_a) {
    var onAddFile = _a.onAddFile, onUploadFile = _a.onUploadFile, file = _a.file;
    return (React.createElement("div", { className: 'Uploader' },
        React.createElement("p", null,
            "File Name: ",
            file ? file.name : ""),
        React.createElement("label", { htmlFor: 'fileUploader' }, "File Uploader"),
        React.createElement("input", { id: 'fileUploader', type: 'file', onChange: onAddFile }),
        React.createElement("br", null),
        React.createElement("button", { type: 'button', onClick: onUploadFile.bind(null, file) }, "Upload"),
        React.createElement("br", null)));
};
exports["default"] = Uploader;
//# sourceMappingURL=Uploader.js.map