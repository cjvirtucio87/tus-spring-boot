"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
const lodash_1 = require("lodash");
const PartInProgress_1 = require("../PartInProgress");
const onMap = progressData => part => {
    const partProgress = lodash_1.find(progressData, record => record.partNumber === part.partNumber);
    return (React.createElement(PartInProgress_1.default, { key: part.partNumber, part: part, partProgress: partProgress }));
};
const UploadProgress = ({ parts, progressData }) => {
    const partNodes = parts ? parts.map(onMap(progressData)) : [];
    return (React.createElement("table", { className: 'UploadProgress table' },
        React.createElement("thead", null,
            React.createElement("tr", null,
                React.createElement("th", null, "Filename"),
                React.createElement("th", null, "Progress"))),
        React.createElement("tbody", null, partNodes)));
};
exports.default = UploadProgress;
//# sourceMappingURL=index.js.map