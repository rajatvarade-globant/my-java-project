const request = require('supertest');
const app = require('../src/server');

describe('GET /', () => {
  it('should return 200 OK', (done) => {
    request(app)
      .get('/')
      .expect(200, done);
  });
  
  it('should return "Hello World"', (done) => {
    request(app)
      .get('/')
      .expect(200)
      .expect('Hello World', done);
  });
});
