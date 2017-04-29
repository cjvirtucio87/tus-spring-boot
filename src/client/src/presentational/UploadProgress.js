"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
const UploadProgress = ({ parts, progressParams }) => {
    const { progress, speed } = progressParams ? progressParams : { progress: 0, speed: 0 };
    return (React.createElement("div", { className: 'UploadProgress' },
        React.createElement("p", null,
            "Uploaded: ",
            progress,
            " %"),
        React.createElement("p", null,
            "Upload Speed: ",
            speed,
            " bytes/sec")));
};
exports.default = UploadProgress;
//# sourceMappingURL=UploadProgress.js.map