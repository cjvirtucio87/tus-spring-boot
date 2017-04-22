var BUILD_FOLDER = './build/static/*';
var TARGET_PATH = '../main/resources/static/';

var spawn = require('child_process').spawn;

console.log(`Moving file ${BUILD_FOLDER} to ${TARGET_PATH}.`);

var cp = spawn('sudo mv', [BUILD_FOLDER, TARGET_PATH]);

cp.on('close', (code, sig) => console.log(`CHILD_PROCESS: code ${code}, sig ${sig}`));