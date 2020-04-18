$(document).ready(function() {
	console.log("Registering events on doc ready...");

	// Define CATEGORY Events
	registerEvents('category', 'span');

	// Define DIRECTION Events
	registerEvents('direction', 'div');
	
	// Define INGREDIENT Events	
	registerEvents('ingredient', 'div', '#input-ingredient-description', '#input-ingredient-amount', '#input-ingredient-uom');
	
	const $editBtn = $('.btn-edit-ingredient');
	$editBtn.click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		toggleEditIngredient(e);
	});
	
});

function registerEvents(elementType, boxType, inputId1, inputId2, inputId3) {
	const elementTypeCapitalized = capitalize(elementType);
	// $('div.ingredient a')!
	$(boxType + '.' + elementType + ' a').click(window['remove' + elementTypeCapitalized]);
	
	const $btn = $('#btn-add-' + elementType);
	const btnHref = $btn.attr('href');
	$btn.click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		if (typeof inputId1 !== 'undefined' && typeof inputId2 !== 'undefined' && typeof inputId3 !== 'undefined') {			
			sendValueToServer(e, btnHref, inputId1, inputId2, inputId3);
		} else {
			sendValueToServer(e, btnHref, '#input-' + elementType);
		}
	});
	
	const $input = $((typeof inputId1 !== 'undefined') ? inputId1 : ('#input-' + elementType));
	$input.on('keypress', function(e) {
		if (e.which === 13) {
			e.preventDefault();
			e.stopPropagation();
			
			if (typeof inputId1 !== 'undefined' && typeof inputId2 !== 'undefined' && typeof inputId3 !== 'undefined') {			
				sendValueToServer(e, btnHref, inputId1, inputId2, inputId3);
			} else {
				sendValueToServer(e, btnHref, '#input-' + elementType);
			}
		}
	});
	
}

function capitalize(str) {
	return str.charAt(0).toUpperCase() + str.substring(1).toLowerCase();
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}


function sendValueToServer(e, postUrl, elementId, elementId2, elementId3) {
	var continueFlag = true;
	var mydata = {}; // data object to store key-value pairs!
	
	var $input = $(elementId);
	var inputVal = $input.val().trim();
	if (inputVal.length == 0) {
		$input.addClass('is-invalid');
		$input.focus();
		continueFlag = false;
	} else {
		mydata['element'] = inputVal;
	}
	
	if (typeof elementId2 !== 'undefined') {
		var $input2 = $(elementId2);
		var inputVal2 = $input2.val().trim();
		if (inputVal2.length == 0) {
			$input2.addClass('is-invalid');
			continueFlag = false;
		} else {
			mydata['element2'] = inputVal2;
		}
	}
	
	if (typeof elementId3 !== 'undefined') {
		var $input3 = $(elementId3);
		var inputVal3 = $input3.val().trim();
		if (inputVal3.length == 0) {
			$input3.addClass('is-invalid');
			continueFlag = false;
		} else {
			mydata['element3'] = inputVal3;
		}
	}
	
	if (!continueFlag) {
		return false;
	}
	
	$.post({
		url : postUrl,
		data : mydata,
		dataType : 'json'
	}).done(function(returnData) {
		if (returnData.status == 'OK') {
			createElement(returnData);
		}
		$input.val('');// clears the field.
	}).fail(function(textStatus, errorThrown) {
		$input.addClass('is-invalid');
	}).always(function() {
		$input.focus();
	});
}

function createElement(data) {
	if (data.type == 'category' || data.type == 'direction' || data.type == 'ingredient') {
		const typeCapitalized = capitalize(data.type);
		//createCategory(data);
		window['create' + typeCapitalized](data);
	} else {
		console.error('Unexpected Element: ' + data);
		alert('Unexpected Element: ' + data);
	}
}


function createCategory(data) {
	var $span = $('#category-');
	var $clonedSpan = $span.clone();
	$clonedSpan.attr('id', 'category-' + data.index);
	$clonedSpan.addClass(isEmpty(data.id) ? 'btn-warning' : 'btn-primary');
	$clonedSpan.find('span').text(data.description);
	var $clonedA = $clonedSpan.find('a');
	var newHref = $clonedA.attr('href') + '?index=' + data.index;
	$clonedA.attr('href', newHref);
	if (!isEmpty(data.id)) {
		$clonedA.attr('data-id', data.id);
		$clonedA.addClass('btn-primary');
	} else {
		$clonedA.addClass('btn-warning');
	}
	$clonedA.click(removeCategory);
	$clonedSpan.appendTo($span.parent());
	$clonedSpan.toggle(true);
};


function removeCategory(e) {
	e.preventDefault();
	e.stopPropagation();
	
    $.post( {url: this.href, 
    		dataType: 'json'})
	    .done(function(data) {
	    	console.log("data: ", data);
	    	if (data.status == 'OK') {
	    		const urlParams = new URL(this.url).searchParams;
	    		$('#category-' + urlParams.get('index')).remove();
	    	} else {
	    		console.error('Data returned: ' + JSON.stringify(data));
	    		alert('Data returned: ' + JSON.stringify(data));
	    	}
	    })
	    .fail(function(textStatus, errorThrown ) {
	    	console.error('Status: ' + JSON.stringify(textStatus) + ', Error: ' + JSON.stringify(errorThrown));
	    });
};


function createDirection(data) {
	var $div = $('#direction-');
	var $clonedDiv = $div.clone();
	$clonedDiv.attr('id', 'direction-' + data.index);
	$clonedDiv.find('span').text((parseInt(data.index) + 1) + '. ' + data.description);
	var $clonedA = $clonedDiv.find('a');
	var newHref = $clonedA.attr('href') + '?index=' + data.index;
	$clonedA.attr('href', newHref);
	$clonedA.attr('data-id', data.id);

	$clonedA.click(removeDirection);
	$clonedDiv.insertBefore('div#direction-input-box');
	$clonedDiv.toggle(true);
};


function removeDirection(e) {
	e.preventDefault();
	e.stopPropagation();
	
    $.post( {url: this.href, 
    		dataType: 'json'})
	    .done(function(data) {
	    	console.log("data: ", data);
	    	if (data.status == 'OK') {
	    		const urlParams = new URL(this.url).searchParams;
	    		$('#direction-' + urlParams.get('index')).remove();
	    	} else {
	    		console.error('Data returned: ' + JSON.stringify(data));
	    		alert('Data returned: ' + JSON.stringify(data));
	    	}
	    })
	    .fail(function(textStatus, errorThrown) {
	    	console.error('Status: ' + JSON.stringify(textStatus) + ', Error: ' + JSON.stringify(errorThrown));
	    });
};


function createIngredient(data) {
	var $div = $('#ingredient-');
	var $clonedDiv = $div.clone();
	$clonedDiv.attr('id', 'ingredient-' + data.index);
	$clonedDiv.find('span').text(data.description);
	var $clonedA = $clonedDiv.find('a');
	var newHref = $clonedA.attr('href') + '?index=' + data.index;
	$clonedA.attr('href', newHref);
	$clonedA.attr('data-id', data.id);

	$clonedA.click(removeIngredient);
	$clonedDiv.insertBefore('div#ingredient-input-box');
	$clonedDiv.toggle(true);
};


function removeIngredient(e) {
	e.preventDefault();
	e.stopPropagation();
	
    $.post( {url: this.href, 
    		dataType: 'json'})
	    .done(function(data) {
	    	console.log("data: ", data);
	    	if (data.status == 'OK') {
	    		const urlParams = new URL(this.url).searchParams;
	    		$('#ingredient-' + urlParams.get('index')).remove();
	    	} else {
	    		console.error('Data returned: ' + JSON.stringify(data));
	    		alert('Data returned: ' + JSON.stringify(data));
	    	}
	    })
	    .fail(function(textStatus, errorThrown) {
	    	console.error('Status: ' + JSON.stringify(textStatus) + ', Error: ' + JSON.stringify(errorThrown));
	    });
};


function editIngredient(e) {
	e.preventDefault();
	e.stopPropagation();
	
    $.post( {url: this.href, 
    		dataType: 'json'})
	    .done(function(data) {
	    	console.log("data: ", data);
	    	if (data.status == 'OK') {
	    		const urlParams = new URL(this.url).searchParams;
	    		$('#ingredient-' + urlParams.get('index')).remove();
	    	} else {
	    		console.error('Data returned: ' + JSON.stringify(data));
	    		alert('Data returned: ' + JSON.stringify(data));
	    	}
	    })
	    .fail(function(textStatus, errorThrown) {
	    	console.error('Status: ' + JSON.stringify(textStatus) + ', Error: ' + JSON.stringify(errorThrown));
	    });
};


function toggleEditIngredient(e) {
	
	
};

