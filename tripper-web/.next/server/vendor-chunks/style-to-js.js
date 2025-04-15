"use strict";
/*
 * ATTENTION: An "eval-source-map" devtool has been used.
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file with attached SourceMaps in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
exports.id = "vendor-chunks/style-to-js";
exports.ids = ["vendor-chunks/style-to-js"];
exports.modules = {

/***/ "(ssr)/./node_modules/style-to-js/cjs/index.js":
/*!***********************************************!*\
  !*** ./node_modules/style-to-js/cjs/index.js ***!
  \***********************************************/
/***/ (function(module, __unused_webpack_exports, __webpack_require__) {

eval("\nvar __importDefault = (this && this.__importDefault) || function (mod) {\n    return (mod && mod.__esModule) ? mod : { \"default\": mod };\n};\nvar style_to_object_1 = __importDefault(__webpack_require__(/*! style-to-object */ \"(ssr)/./node_modules/style-to-object/cjs/index.js\"));\nvar utilities_1 = __webpack_require__(/*! ./utilities */ \"(ssr)/./node_modules/style-to-js/cjs/utilities.js\");\n/**\n * Parses CSS inline style to JavaScript object (camelCased).\n */\nfunction StyleToJS(style, options) {\n    var output = {};\n    if (!style || typeof style !== 'string') {\n        return output;\n    }\n    (0, style_to_object_1.default)(style, function (property, value) {\n        // skip CSS comment\n        if (property && value) {\n            output[(0, utilities_1.camelCase)(property, options)] = value;\n        }\n    });\n    return output;\n}\nStyleToJS.default = StyleToJS;\nmodule.exports = StyleToJS;\n//# sourceMappingURL=index.js.map//# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiKHNzcikvLi9ub2RlX21vZHVsZXMvc3R5bGUtdG8tanMvY2pzL2luZGV4LmpzIiwibWFwcGluZ3MiOiJBQUFhO0FBQ2I7QUFDQSw2Q0FBNkM7QUFDN0M7QUFDQSx3Q0FBd0MsbUJBQU8sQ0FBQywwRUFBaUI7QUFDakUsa0JBQWtCLG1CQUFPLENBQUMsc0VBQWE7QUFDdkM7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxLQUFLO0FBQ0w7QUFDQTtBQUNBO0FBQ0E7QUFDQSIsInNvdXJjZXMiOlsiL2hvbWUvdml0b3JsZnJlaXRhcy9PbmVEcml2ZS9Eb2N1bWVudG9zL0dpdEh1Yi90cmlwcGVyLWZyb250ZW5kL25vZGVfbW9kdWxlcy9zdHlsZS10by1qcy9janMvaW5kZXguanMiXSwic291cmNlc0NvbnRlbnQiOlsiXCJ1c2Ugc3RyaWN0XCI7XG52YXIgX19pbXBvcnREZWZhdWx0ID0gKHRoaXMgJiYgdGhpcy5fX2ltcG9ydERlZmF1bHQpIHx8IGZ1bmN0aW9uIChtb2QpIHtcbiAgICByZXR1cm4gKG1vZCAmJiBtb2QuX19lc01vZHVsZSkgPyBtb2QgOiB7IFwiZGVmYXVsdFwiOiBtb2QgfTtcbn07XG52YXIgc3R5bGVfdG9fb2JqZWN0XzEgPSBfX2ltcG9ydERlZmF1bHQocmVxdWlyZShcInN0eWxlLXRvLW9iamVjdFwiKSk7XG52YXIgdXRpbGl0aWVzXzEgPSByZXF1aXJlKFwiLi91dGlsaXRpZXNcIik7XG4vKipcbiAqIFBhcnNlcyBDU1MgaW5saW5lIHN0eWxlIHRvIEphdmFTY3JpcHQgb2JqZWN0IChjYW1lbENhc2VkKS5cbiAqL1xuZnVuY3Rpb24gU3R5bGVUb0pTKHN0eWxlLCBvcHRpb25zKSB7XG4gICAgdmFyIG91dHB1dCA9IHt9O1xuICAgIGlmICghc3R5bGUgfHwgdHlwZW9mIHN0eWxlICE9PSAnc3RyaW5nJykge1xuICAgICAgICByZXR1cm4gb3V0cHV0O1xuICAgIH1cbiAgICAoMCwgc3R5bGVfdG9fb2JqZWN0XzEuZGVmYXVsdCkoc3R5bGUsIGZ1bmN0aW9uIChwcm9wZXJ0eSwgdmFsdWUpIHtcbiAgICAgICAgLy8gc2tpcCBDU1MgY29tbWVudFxuICAgICAgICBpZiAocHJvcGVydHkgJiYgdmFsdWUpIHtcbiAgICAgICAgICAgIG91dHB1dFsoMCwgdXRpbGl0aWVzXzEuY2FtZWxDYXNlKShwcm9wZXJ0eSwgb3B0aW9ucyldID0gdmFsdWU7XG4gICAgICAgIH1cbiAgICB9KTtcbiAgICByZXR1cm4gb3V0cHV0O1xufVxuU3R5bGVUb0pTLmRlZmF1bHQgPSBTdHlsZVRvSlM7XG5tb2R1bGUuZXhwb3J0cyA9IFN0eWxlVG9KUztcbi8vIyBzb3VyY2VNYXBwaW5nVVJMPWluZGV4LmpzLm1hcCJdLCJuYW1lcyI6W10sImlnbm9yZUxpc3QiOlswXSwic291cmNlUm9vdCI6IiJ9\n//# sourceURL=webpack-internal:///(ssr)/./node_modules/style-to-js/cjs/index.js\n");

/***/ }),

/***/ "(ssr)/./node_modules/style-to-js/cjs/utilities.js":
/*!***************************************************!*\
  !*** ./node_modules/style-to-js/cjs/utilities.js ***!
  \***************************************************/
/***/ ((__unused_webpack_module, exports) => {

eval("\nObject.defineProperty(exports, \"__esModule\", ({ value: true }));\nexports.camelCase = void 0;\nvar CUSTOM_PROPERTY_REGEX = /^--[a-zA-Z0-9_-]+$/;\nvar HYPHEN_REGEX = /-([a-z])/g;\nvar NO_HYPHEN_REGEX = /^[^-]+$/;\nvar VENDOR_PREFIX_REGEX = /^-(webkit|moz|ms|o|khtml)-/;\nvar MS_VENDOR_PREFIX_REGEX = /^-(ms)-/;\n/**\n * Checks whether to skip camelCase.\n */\nvar skipCamelCase = function (property) {\n    return !property ||\n        NO_HYPHEN_REGEX.test(property) ||\n        CUSTOM_PROPERTY_REGEX.test(property);\n};\n/**\n * Replacer that capitalizes first character.\n */\nvar capitalize = function (match, character) {\n    return character.toUpperCase();\n};\n/**\n * Replacer that removes beginning hyphen of vendor prefix property.\n */\nvar trimHyphen = function (match, prefix) { return \"\".concat(prefix, \"-\"); };\n/**\n * CamelCases a CSS property.\n */\nvar camelCase = function (property, options) {\n    if (options === void 0) { options = {}; }\n    if (skipCamelCase(property)) {\n        return property;\n    }\n    property = property.toLowerCase();\n    if (options.reactCompat) {\n        // `-ms` vendor prefix should not be capitalized\n        property = property.replace(MS_VENDOR_PREFIX_REGEX, trimHyphen);\n    }\n    else {\n        // for non-React, remove first hyphen so vendor prefix is not capitalized\n        property = property.replace(VENDOR_PREFIX_REGEX, trimHyphen);\n    }\n    return property.replace(HYPHEN_REGEX, capitalize);\n};\nexports.camelCase = camelCase;\n//# sourceMappingURL=utilities.js.map//# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiKHNzcikvLi9ub2RlX21vZHVsZXMvc3R5bGUtdG8tanMvY2pzL3V0aWxpdGllcy5qcyIsIm1hcHBpbmdzIjoiQUFBYTtBQUNiLDhDQUE2QyxFQUFFLGFBQWEsRUFBQztBQUM3RCxpQkFBaUI7QUFDakI7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSw0Q0FBNEM7QUFDNUM7QUFDQTtBQUNBO0FBQ0E7QUFDQSw4QkFBOEI7QUFDOUI7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGlCQUFpQjtBQUNqQiIsInNvdXJjZXMiOlsiL2hvbWUvdml0b3JsZnJlaXRhcy9PbmVEcml2ZS9Eb2N1bWVudG9zL0dpdEh1Yi90cmlwcGVyLWZyb250ZW5kL25vZGVfbW9kdWxlcy9zdHlsZS10by1qcy9janMvdXRpbGl0aWVzLmpzIl0sInNvdXJjZXNDb250ZW50IjpbIlwidXNlIHN0cmljdFwiO1xuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xuZXhwb3J0cy5jYW1lbENhc2UgPSB2b2lkIDA7XG52YXIgQ1VTVE9NX1BST1BFUlRZX1JFR0VYID0gL14tLVthLXpBLVowLTlfLV0rJC87XG52YXIgSFlQSEVOX1JFR0VYID0gLy0oW2Etel0pL2c7XG52YXIgTk9fSFlQSEVOX1JFR0VYID0gL15bXi1dKyQvO1xudmFyIFZFTkRPUl9QUkVGSVhfUkVHRVggPSAvXi0od2Via2l0fG1venxtc3xvfGtodG1sKS0vO1xudmFyIE1TX1ZFTkRPUl9QUkVGSVhfUkVHRVggPSAvXi0obXMpLS87XG4vKipcbiAqIENoZWNrcyB3aGV0aGVyIHRvIHNraXAgY2FtZWxDYXNlLlxuICovXG52YXIgc2tpcENhbWVsQ2FzZSA9IGZ1bmN0aW9uIChwcm9wZXJ0eSkge1xuICAgIHJldHVybiAhcHJvcGVydHkgfHxcbiAgICAgICAgTk9fSFlQSEVOX1JFR0VYLnRlc3QocHJvcGVydHkpIHx8XG4gICAgICAgIENVU1RPTV9QUk9QRVJUWV9SRUdFWC50ZXN0KHByb3BlcnR5KTtcbn07XG4vKipcbiAqIFJlcGxhY2VyIHRoYXQgY2FwaXRhbGl6ZXMgZmlyc3QgY2hhcmFjdGVyLlxuICovXG52YXIgY2FwaXRhbGl6ZSA9IGZ1bmN0aW9uIChtYXRjaCwgY2hhcmFjdGVyKSB7XG4gICAgcmV0dXJuIGNoYXJhY3Rlci50b1VwcGVyQ2FzZSgpO1xufTtcbi8qKlxuICogUmVwbGFjZXIgdGhhdCByZW1vdmVzIGJlZ2lubmluZyBoeXBoZW4gb2YgdmVuZG9yIHByZWZpeCBwcm9wZXJ0eS5cbiAqL1xudmFyIHRyaW1IeXBoZW4gPSBmdW5jdGlvbiAobWF0Y2gsIHByZWZpeCkgeyByZXR1cm4gXCJcIi5jb25jYXQocHJlZml4LCBcIi1cIik7IH07XG4vKipcbiAqIENhbWVsQ2FzZXMgYSBDU1MgcHJvcGVydHkuXG4gKi9cbnZhciBjYW1lbENhc2UgPSBmdW5jdGlvbiAocHJvcGVydHksIG9wdGlvbnMpIHtcbiAgICBpZiAob3B0aW9ucyA9PT0gdm9pZCAwKSB7IG9wdGlvbnMgPSB7fTsgfVxuICAgIGlmIChza2lwQ2FtZWxDYXNlKHByb3BlcnR5KSkge1xuICAgICAgICByZXR1cm4gcHJvcGVydHk7XG4gICAgfVxuICAgIHByb3BlcnR5ID0gcHJvcGVydHkudG9Mb3dlckNhc2UoKTtcbiAgICBpZiAob3B0aW9ucy5yZWFjdENvbXBhdCkge1xuICAgICAgICAvLyBgLW1zYCB2ZW5kb3IgcHJlZml4IHNob3VsZCBub3QgYmUgY2FwaXRhbGl6ZWRcbiAgICAgICAgcHJvcGVydHkgPSBwcm9wZXJ0eS5yZXBsYWNlKE1TX1ZFTkRPUl9QUkVGSVhfUkVHRVgsIHRyaW1IeXBoZW4pO1xuICAgIH1cbiAgICBlbHNlIHtcbiAgICAgICAgLy8gZm9yIG5vbi1SZWFjdCwgcmVtb3ZlIGZpcnN0IGh5cGhlbiBzbyB2ZW5kb3IgcHJlZml4IGlzIG5vdCBjYXBpdGFsaXplZFxuICAgICAgICBwcm9wZXJ0eSA9IHByb3BlcnR5LnJlcGxhY2UoVkVORE9SX1BSRUZJWF9SRUdFWCwgdHJpbUh5cGhlbik7XG4gICAgfVxuICAgIHJldHVybiBwcm9wZXJ0eS5yZXBsYWNlKEhZUEhFTl9SRUdFWCwgY2FwaXRhbGl6ZSk7XG59O1xuZXhwb3J0cy5jYW1lbENhc2UgPSBjYW1lbENhc2U7XG4vLyMgc291cmNlTWFwcGluZ1VSTD11dGlsaXRpZXMuanMubWFwIl0sIm5hbWVzIjpbXSwiaWdub3JlTGlzdCI6WzBdLCJzb3VyY2VSb290IjoiIn0=\n//# sourceURL=webpack-internal:///(ssr)/./node_modules/style-to-js/cjs/utilities.js\n");

/***/ })

};
;