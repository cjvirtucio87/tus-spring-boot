"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
const PartInProgress = ({ part, partProgress }) => (React.createElement("tr", { className: 'PartInProgress' },
    React.createElement("td", null,
        "Filename: ",
        `${part.fileName}_${part.partNumber}`),
    React.createElement("td", null,
        "Uploaded: ",
        partProgress)));
exports.default = PartInProgress;
//# sourceMappingURL=index.js.map