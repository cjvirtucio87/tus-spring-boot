import * as React from 'react';
import Uploader from './Uploader';
import './App.css';

interface IAppState {

}

interface IAppProps {

}

class App extends React.Component<IAppState, IAppProps> {
  render() {
    return (
      <div className="App">
        <div className="App-header">
          <h2>Welcome to React</h2>
        </div>
        <p className="App-intro">
          To get started, edit <code>src/App.js</code> and save to reload.
        </p>

        <Uploader/>
      </div>
    );
  }
}

export default App;
