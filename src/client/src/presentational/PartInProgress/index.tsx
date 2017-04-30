import * as React from 'react';
import { find } from 'lodash';

const PartInProgress = ({ part, partProgress }) => (
  <tr className='PartInProgress'>
    <td>Filename: { `${part.fileName}_${part.partNumber}` }</td>
    <td>Uploaded: { partProgress }</td>
  </tr>
);

export default PartInProgress;