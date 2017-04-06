$(function(){

    var processNumbers = function(algorithm, numbers, $resultElement){
        $.ajax({
            url: 'processNumbers',
            type: 'POST',
            data: {
                algorithm: algorithm,
                numbers: numbers
            }
        }).then(function(sum) {
            $resultElement.text($resultElement.text() + sum);
        }, function(error){
            $resultElement.addClass('error').text($resultElement.text() + eval(error.responseJSON).message);
        });
    };

    $('#submit').click(function(){
        var $result = $('#result').html('');
        var rawMultilineNumbers = $('#numbers').val().split('\n');
        for(var i=0;i<rawMultilineNumbers.length;i++){
            var $resultDiv = $('<div></div>');
            $result.append($resultDiv);
            var rawNumbers = rawMultilineNumbers[i];
            if(/^[\d,\s\-]*$/.test(rawNumbers)===false){
                $resultDiv.addClass('error').text('Invalid symbols');
                continue;
            }
            var numbers = rawNumbers.split(',').map(Number);
            if(numbers.length>globals.MAX_NUMBERS_COUNT){
                $resultDiv.addClass('error').text('Too many numbers');
                continue;
            }
            if(numbers.some(function(x){return x>globals.MAX_NUMBER || x<globals.MIN_NUMBER})){
                $resultDiv.addClass('error').text('At least one number >'+globals.MAX_NUMBER+' or <'+globals.MIN_NUMBER);
                continue;
            }
            var $firstResult = $('<span class="first">1: </span>');
            processNumbers('FIRST', numbers, $firstResult);
            var $secondResult = $('<span class="second">2: </span>');
            processNumbers('SECOND', numbers, $secondResult);

            $resultDiv.append($firstResult).append($secondResult);
        }
    });

});