"use strict";
exports.__esModule = true;
var React = require("react");
var react_redux_1 = require("react-redux");
var actions_1 = require("../actions");
var _1 = require("../presentational/");
var axios_1 = require("axios");
var Uploader = _1["default"].Uploader, UploadProgress = _1["default"].UploadProgress;
var mapStateToProps = function (state) { return ({
    file: state.file,
    progress: state.progress
}); };
var mapDispatchToProps = function (dispatch) { return ({
    onAddFile: function (event) {
        var reader = new FileReader();
        var file = event.target.files[0];
        reader.onloadend = function () { return dispatch(actions_1.addFile(file)); };
        reader.readAsDataURL(file);
    },
    onUploadFile: function (file) {
        axios_1["default"].patch("http://localhost:8080/upload/" + file.name.slice(0, file.name.length - 4), file, {
            headers: {
                'content-type': 'text/plain',
                fileName: file.name.slice(0, file.name.length - 4),
                partNumber: 1,
                uploadLength: file.size,
                userName: 'cjvirtucio'
            },
            onUploadProgress: function (ev) {
                var progress = Math.floor((ev.loaded / file.size) * 100);
                dispatch(actions_1.updateProgress(progress));
                if (progress === 100)
                    dispatch(actions_1.uploadFile(file));
            }
        })["catch"](function (err) { return console.log(err); });
    }
}); };
var Dashboard = function (_a) {
    var onAddFile = _a.onAddFile, onUploadFile = _a.onUploadFile, file = _a.file, progress = _a.progress;
    return (React.createElement("div", { className: 'Dashboard' },
        React.createElement(Uploader, { onAddFile: onAddFile, onUploadFile: onUploadFile, file: file }),
        React.createElement(UploadProgress, { progress: progress })));
};
exports["default"] = react_redux_1.connect(mapStateToProps, mapDispatchToProps)(Dashboard);
//# sourceMappingURL=Dashboard.js.map