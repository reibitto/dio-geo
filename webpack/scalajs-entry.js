if (process.env.NODE_ENV === "production") {
    const opt = require("./dio-geo-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./dio-geo-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./dio-geo-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
