const helloWorld = require("../helloworld");

describe("helloWorld", () => {
  it("should return 'Hello, World!'", () => {
    expect(helloWorld()).toEqual("Hello, World!");
  });
});
