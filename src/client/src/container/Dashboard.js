"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
const react_redux_1 = require("react-redux");
const constants_1 = require("../constants");
const actions_1 = require("../actions");
const _1 = require("../presentational/");
const moment = require("moment");
const { Uploader, UploadProgress } = _1.default;
const computeProgress = (loaded, fileSize) => Math.floor((loaded / fileSize) * 100);
const computeElapsedTime = (unit) => (startTime) => moment().diff(startTime, unit) || 1;
const computeElapsedSeconds = computeElapsedTime('seconds');
const computeSpeed = (loaded, startTime) => Math.floor(loaded / computeElapsedSeconds(startTime));
const capAtFilesize = (value, fileSize) => value > fileSize ? fileSize : value;
const createFileParts = (file, uploadOffset, uploadLength, partCount, parts) => {
    if (uploadOffset >= file.size)
        return parts;
    parts.push({
        file: file.slice(uploadOffset, uploadLength + 1),
        partNum: partCount,
        uploadOffset: capAtFilesize(uploadOffset, file.size),
        uploadLength: capAtFilesize(uploadLength, file.size)
    });
    return createFileParts(file, uploadOffset + constants_1.PART_SIZE, uploadLength + constants_1.PART_SIZE, partCount + 1, parts);
};
const onLoadEnd = (dispatch, file) => () => {
    const parts = createFileParts(file, 0, constants_1.PART_SIZE, 0, []);
    console.log(parts);
    dispatch(actions_1.addFile(parts));
};
const onAddFile = dispatch => (event) => {
    const reader = new FileReader();
    const file = event.target.files[0];
    reader.onloadend = onLoadEnd(dispatch, file);
    reader.readAsDataURL(file);
};
const mapStateToProps = (state) => ({
    file: state.file,
    progressParams: state.progressParams
});
const mapDispatchToProps = (dispatch) => ({
    onAddFile: onAddFile(dispatch),
});
const Dashboard = ({ onAddFile, parts, progressParams }) => (React.createElement("div", { className: 'Dashboard' },
    React.createElement(Uploader, { onAddFile: onAddFile, parts: parts }),
    React.createElement(UploadProgress, { parts: parts, progressParams: progressParams })));
exports.default = react_redux_1.connect(mapStateToProps, mapDispatchToProps)(Dashboard);
//# sourceMappingURL=Dashboard.js.map