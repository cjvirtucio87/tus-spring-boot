"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
const Part = ({ part }) => React.createElement("p", null,
    "File Part: ",
    part.file.name);
const Uploader = ({ onAddFile, parts }) => {
    const partNodes = parts ? parts.map(part => React.createElement(Part, { part: part })) : React.createElement("p", null, "No uploads yet");
    return (React.createElement("div", { className: 'Uploader' },
        partNodes,
        React.createElement("label", { htmlFor: 'fileUploader' }, "File Uploader"),
        React.createElement("input", { id: 'fileUploader', type: 'file', onChange: onAddFile }),
        React.createElement("br", null)));
};
exports.default = Uploader;
//# sourceMappingURL=Uploader.js.map