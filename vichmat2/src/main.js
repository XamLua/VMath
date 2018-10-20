var calculator;

window.onload = function () {
    var elt = document.getElementById('calculator');
    calculator = Desmos.GraphingCalculator(elt, {
        keypad: false,
        settingsMenu: false,
        expressions: false
    });
};

updateEquation = function (id, func, showLabel, label, color = Desmos.Colors.BLUE) {
    calculator.setExpression({id: id, latex: func, label: label, showLabel: showLabel, color: color});
};