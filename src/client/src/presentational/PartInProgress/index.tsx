import * as React from 'react';
import { find } from 'lodash';

const PartInProgress = ({ part, progress, speed }) => (
  <tr className='PartInProgress'>
    <td>{ `${part.fileName}_${part.partNumber}` }</td>
    <td>{ progress ? progress : 0 }%</td>
    <td>{ speed ? speed : 0 } bytes/sec</td>
  </tr>
);

export default PartInProgress;