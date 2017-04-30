import * as React from 'react';
import { find } from 'lodash';
import PartInProgress from '../PartInProgress';

const onMap = progressData => part => {
  const partProgress = find(progressData, record => record.partNumber === part.partNumber);

  return (
    <PartInProgress 
    key={ part.partNumber }
    part={ part }
    partProgress={ partProgress }
    />
  );
}

const UploadProgress = ({ parts, progressData }) => {
  const partNodes = parts ? parts.map(onMap(progressData)) : [];

  return (
    <table className='UploadProgress table'>
      <thead>
        <tr>
          <th>Filename</th>
          <th>Progress</th>
        </tr>
      </thead>
      <tbody>
        { partNodes }
      </tbody>
    </table>
  )
};

export default UploadProgress;