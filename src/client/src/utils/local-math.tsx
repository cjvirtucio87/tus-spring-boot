import * as moment from 'moment';

export const computeElapsedTime = (unit) => (startTime) => moment().diff(startTime, unit) || 1;