/* A store is created by passing your reducer to the createStore function. */

import { file } from '../reducers';
import { createStore } from 'redux';

const store = createStore(file);

export default store;