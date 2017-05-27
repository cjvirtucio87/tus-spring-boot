import * as moment from 'moment';

export const computeProgress = (loaded, fileSize) => Math.floor((loaded / fileSize) * 100);

export const computeElapsedTime = (unit) => (startTime) => moment().diff(startTime, unit) || 1;