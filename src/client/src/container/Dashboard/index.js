"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
const react_redux_1 = require("react-redux");
const constants_1 = require("../../constants");
const actions_1 = require("../../actions");
const _1 = require("../../presentational/");
const axios_1 = require("axios");
const moment = require("moment");
const Rx = require("rxjs");
require("./style.css");
const { Uploader, UploadProgress } = _1.default;
const computeProgress = (loaded, fileSize) => Math.floor((loaded / fileSize) * 100);
const computeElapsedTime = (unit) => (startTime) => moment().diff(startTime, unit) || 1;
const computeElapsedSeconds = computeElapsedTime('seconds');
const computeSpeed = (loaded, startTime) => Math.floor(loaded / computeElapsedSeconds(startTime));
const capAtFilesize = (value, fileSize) => value > fileSize ? fileSize : value;
const createFileParts = (file, fileName, uploadOffset, uploadLength, partNumber, parts) => {
    if (uploadOffset >= file.size)
        return parts;
    parts.push({
        file: file.slice(uploadOffset, uploadLength + 1),
        fileName,
        partNumber,
        uploadOffset: capAtFilesize(uploadOffset, file.size),
        uploadLength: capAtFilesize(uploadLength, file.size)
    });
    return createFileParts(file, fileName, uploadOffset + constants_1.PART_SIZE, uploadLength + constants_1.PART_SIZE, partNumber + 1, parts);
};
const onLoadEnd = (dispatch, file) => () => {
    const fileName = /^(.+)\..*/.exec(file.name)[1];
    const parts = createFileParts(file, fileName, 0, constants_1.PART_SIZE, 0, []);
    console.log(`Created file parts for file, ${file.name}`);
    console.log(parts);
    dispatch(actions_1.addFile(parts));
};
const onAddFile = dispatch => event => {
    const reader = new FileReader();
    const file = event.target.files[0];
    reader.onloadend = onLoadEnd(dispatch, file);
    reader.readAsDataURL(file);
};
const uploadPart = dispatch => startTime => part => {
    const { partNumber, uploadLength, file, fileName } = part;
    return axios_1.default.patch(`http://localhost:8080/upload/${fileName}`, file, {
        headers: {
            'content-type': 'text/plain',
            fileName,
            partNumber,
            uploadLength,
            userName: 'cjvirtucio'
        },
        onUploadProgress(ev) {
            const progress = computeProgress(ev.loaded, file.size);
            const speed = computeSpeed(ev.loaded, startTime);
            dispatch(actions_1.updateProgress({ partNumber, progress, speed }));
        }
    });
};
const onUploadFile = dispatch => parts => event => {
    const startTime = moment();
    Rx.Observable.from(parts)
        .subscribe(uploadPart(dispatch)(startTime));
};
const mapStateToProps = (state) => ({
    file: state.file,
    parts: state.parts,
    progressData: state.progressData
});
const mapDispatchToProps = (dispatch) => ({
    onAddFile: onAddFile(dispatch),
    onUploadFile: onUploadFile(dispatch)
});
const Dashboard = ({ onAddFile, parts, progressData }) => (React.createElement("div", { className: 'Dashboard container-fluid' },
    React.createElement("section", { className: 'row align-items-center justify-content-center' },
        React.createElement("div", { className: 'col-4' },
            React.createElement("h3", null, "File Uploader"),
            React.createElement("p", null, "Upload a file. The client slices it into chunks and will fire off a request for each one when you hit the upload button.")),
        React.createElement("div", { className: 'col-4' },
            React.createElement(Uploader, { onAddFile: onAddFile, onUploadFile: onUploadFile, parts: parts }))),
    React.createElement("section", { className: 'row align-items-center justify-content-center' },
        React.createElement("div", { className: 'col-4' },
            React.createElement("h3", null, "Upload Progress"),
            React.createElement("p", null, "This component will reveal a table showing the progress of each chunk.")),
        React.createElement("div", { className: 'col-4' },
            React.createElement(UploadProgress, { parts: parts, progressData: progressData })))));
exports.default = react_redux_1.connect(mapStateToProps, mapDispatchToProps)(Dashboard);
//# sourceMappingURL=index.js.map