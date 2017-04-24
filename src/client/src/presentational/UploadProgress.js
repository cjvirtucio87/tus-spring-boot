"use strict";
exports.__esModule = true;
var React = require("react");
var UploadProgress = function (_a) {
    var progressParams = _a.progressParams;
    var _b = progressParams ? progressParams : { progress: 0, speed: 0 }, progress = _b.progress, speed = _b.speed;
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
exports["default"] = UploadProgress;
//# sourceMappingURL=UploadProgress.js.map