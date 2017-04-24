"use strict";
exports.__esModule = true;
var React = require("react");
var react_redux_1 = require("react-redux");
var actions_1 = require("../actions");
var _1 = require("../presentational/");
var axios_1 = require("axios");
var moment = require("moment");
var Uploader = _1["default"].Uploader, UploadProgress = _1["default"].UploadProgress;
var computeProgress = function (loaded, fileSize) { return Math.floor((loaded / fileSize) * 100); };
var computeSpeed = function (loaded, startTime) { return Math.floor(loaded / parseInt(moment().subtract(startTime).format('SS'))); };
var onUploadFile = function (dispatch) { return function (file) {
    var startTime = parseInt(moment().format('SS'));
    axios_1["default"].patch("http://localhost:8080/upload/" + file.name.slice(0, file.name.length - 4), file, {
        headers: {
            'content-type': 'text/plain',
            fileName: file.name.slice(0, file.name.length - 4),
            partNumber: 1,
            uploadLength: file.size,
            userName: 'cjvirtucio'
        },
        onUploadProgress: function (ev) {
            var progress = computeProgress(ev.loaded, file.size);
            var speed = computeSpeed(ev.loaded, startTime);
            console.log(speed);
            dispatch(actions_1.updateProgress({ progress: progress, speed: speed }));
            if (progress === 100)
                dispatch(actions_1.uploadFile(file));
        }
    })["catch"](function (err) { return console.log(err); });
}; };
var onAddFile = function (dispatch) { return function (event) {
    var reader = new FileReader();
    var file = event.target.files[0];
    reader.onloadend = function () { return dispatch(actions_1.addFile(file)); };
    reader.readAsDataURL(file);
}; };
var mapStateToProps = function (state) { return ({
    file: state.file,
    progressParams: state.progressParams
}); };
var mapDispatchToProps = function (dispatch) { return ({
    onAddFile: onAddFile(dispatch),
    onUploadFile: onUploadFile(dispatch)
}); };
var Dashboard = function (_a) {
    var onAddFile = _a.onAddFile, onUploadFile = _a.onUploadFile, file = _a.file, progressParams = _a.progressParams;
    return (React.createElement("div", { className: 'Dashboard' },
        React.createElement(Uploader, { onAddFile: onAddFile, onUploadFile: onUploadFile, file: file }),
        React.createElement(UploadProgress, { progressParams: progressParams })));
};
exports["default"] = react_redux_1.connect(mapStateToProps, mapDispatchToProps)(Dashboard);
//# sourceMappingURL=Dashboard.js.map