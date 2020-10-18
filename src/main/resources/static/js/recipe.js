$(document).ready(function() {
	registerEvents("category", "span", "description");
	registerEvents("direction", "div", "direction");
	registerIngredientEvents();
	console.log("Registered category, direction, ingredient events on doc ready.");
});

function registerEvents(elementType, boxType, description) {
	const elementTypeCapitalized = capitalize(elementType);
	// $("div.ingredient a")!
	$(boxType + "." + elementType + " a").click(window["delete" + elementTypeCapitalized]);
	
	const $btn = $("#btn-add-" + elementType);
	const btnHref = $btn.attr("href");
	$btn.click(function(e) {
		e.preventDefault();
		e.stopPropagation();

		sendValueToServer(e, btnHref, elementType, description);
	});
	
	const $input = $("#input-" + elementType);
	$input.on("keypress", function(e) {
		if (e.which === 13) {
			e.preventDefault();
			e.stopPropagation();
			
			sendValueToServer(e, btnHref, elementType, description);
		}
	});
	
}


function capitalize(str) {
	return str.charAt(0).toUpperCase() + str.substring(1).toLowerCase();
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}


function sendValueToServer(e, postUrl, elementType, description) {	
	var myData = {};
	
	const inputId = "#input-" + elementType;
	var $input = $(inputId);
	var inputVal = $input.val().trim();
	if (inputVal.length == 0) {
		$input.addClass("is-invalid");
		$input.focus();
		return;
	} else {
		$input.removeClass("is-invalid");
		
		myData["id"] = null;
		myData[description] = inputVal;
	}
	
	postData(postUrl, myData)
    .then(
		(responseData) => {
			// JSON data parsed by "response.json()" call
			console.log(responseData); 
			if (responseData.status == "OK") {
				if (responseData.type == "category") {
					createCategory(responseData);
				} else if (responseData.type == "direction") {
					createDirection(responseData);
				} else {
					console.error("Unexpected Element: " + responseData);
					alert("Unexpected Element: " + responseData);
				}
			}
			$input.val("");// clears the field.
			$input.focus();
		}, 
		(error) => {
			console.error(error); 
			$input.focus();
		}
    );

}


function createCategory(data) {
	var $span = $("#category-");
	var $clonedSpan = $span.clone();
	$clonedSpan.attr("id", "category-" + data.id);
	$clonedSpan.addClass(isEmpty(data.id) ? "btn-warning" : "btn-primary");
	$clonedSpan.find("span").text(data.description);
	
	var recipeId = $('input#id').val();
	var newHref = '/recipe/' + recipeId + '/category/' + data.id + '/delete';
	
	var $clonedA = $clonedSpan.find("a");
	$clonedA.attr("href", newHref);
	if (!isEmpty(data.id)) {
		$clonedA.attr("data-id", data.id);
		$clonedA.addClass("btn-primary");
	} else {
		$clonedA.addClass("btn-warning");
	}
	$clonedA.click(deleteCategory);
	$clonedSpan.appendTo($span.parent());
	$clonedSpan.toggle(true);
};


function deleteCategory(e) {
	e.preventDefault();
	e.stopPropagation();
	
	var myData = {};
	// e.g. href = '/recipe/1/category/2/delete';
	var pattern = /\d+(?=\/delete)/gi; 
	myData["id"] = pattern.exec(this.href);
	
	postData(this.href, myData)
    .then(
		(responseData) => {
			// JSON data parsed by "response.json()" call
			console.log(responseData); 
			if (responseData.status == "OK" && responseData.type == "category") {
	    		$("#category-" + myData.id).remove();	
			} else {
				console.error("Unexpected Element: " + responseData);
			}
		}, 
		(error) => {
			console.error(error); 
		}
    );
	
};


function createDirection(data) {
	var $div = $("#direction-");
	var $clonedDiv = $div.clone();
	$clonedDiv.attr("id", "direction-" + data.id);
	$clonedDiv.find("span").text((parseInt(data.id) + 1) + ". " + data.description);
	var $clonedA = $clonedDiv.find("a");
	var newHref = $clonedA.attr("href") + "?index=" + data.id;
	$clonedA.attr("href", newHref);
	$clonedA.attr("data-id", data.id);

	$clonedA.click(deleteDirection);
	$clonedDiv.insertBefore("div#direction-input-box");
	$clonedDiv.toggle(true);
};


function deleteDirection(e) {
	e.preventDefault();
	e.stopPropagation();
	
    $.post( {url: this.href, 
    		dataType: "json"})
	    .done(function(data) {
	    	console.log("data: ", data);
	    	if (data.status == "OK") {
	    		const urlParams = new URL(this.url).searchParams;
	    		$("#direction-" + urlParams.get("index")).remove();
	    	} else {
	    		console.error("Data returned: " + JSON.stringify(data));
	    		alert("Data returned: " + JSON.stringify(data));
	    	}
	    })
	    .fail(function(textStatus, errorThrown) {
	    	console.error("Status: " + JSON.stringify(textStatus) + ", Error: " + JSON.stringify(errorThrown));
	    });
};



function registerIngredientEvents() {
	const $divIngredient = $("div.ingredient");
	
	// Multiple "a" tags are to be selected for delete button clicks. 
	$divIngredient.find("a.btn-delete-ingredient").click(deleteIngredient);
	
	$divIngredient.find("a.btn-edit-ingredient").click(startEditingIngredient);
	
	
	const $inputBox = $("div#ingredient-input-box");
	// A single "a" tag is to be selected for add button. 
	const $addBtn = $inputBox.find("a#btn-add-ingredient");
	$addBtn.click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		var myData = getIngredientInputValues(e);
		if (myData === null) {
			return false;
		}
			
	    postData(e.currentTarget.href, myData)
	    .then(
    		(responseData) => {
    			// JSON data parsed by "response.json()" call
    			console.log(responseData); 
    			createIngredient(responseData);
    			$("a#btn-cancel-update-ingredient").click();
    		}, 
    		(error) => {
    			console.error(error); 
    		}
	    );
	    
	});
	
	
	const $updateBtn = $("a#btn-update-ingredient");
	$updateBtn.click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		var myData = getIngredientInputValues(e);
		if (myData === null) {
			return false;
		}
		
	    postData(e.currentTarget.href, myData)
	    .then(
	    		(responseData) => {
	    			// JSON data parsed by `response.json()` call
	    			console.log(responseData); 
	    			updateIngredient(responseData);
	    			$("a#btn-cancel-update-ingredient").click();
	    		}, 
	    		(error) => {
	    			console.error(error); 
	    		}
	    );
	    
	});
	
	
	$("input#input-ingredient-description").on("keypress", function(e) {
		if (e.which == 13) {
			e.preventDefault();
			e.stopPropagation();
			// Pressing Enter in "description" field will trigger 
			// the click event of add button.
			if (!$addBtn.hasClass("d-none")) {
				$addBtn.click();
			} else if (!$updateBtn.hasClass("d-none")) {
				$updateBtn.click();
			} else {
				console.error("Wrong state: Either 'add' or 'update' button was supposed to be visible!");
			}
		}
	});
		

	$("a#btn-cancel-update-ingredient").click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		// Finish editing mode. 
		$divIngredient.removeClass("highlight");
		
		$inputBox.removeClass("highlight");
		
		// Clear "amount", "uom" and "description" fields.
		$inputBox.find("input, select").val("");
		
		$inputBox.find("a#btn-add-ingredient").removeClass("d-none");
		$inputBox.find("a#btn-update-ingredient, a#btn-cancel-update-ingredient").addClass("d-none");
	});
}


function getInputValue(inputId, focusIfInvalid = false) {
	const $input = $(inputId);
	const inputVal = $input.val();
	if (inputVal == null || inputVal.trim().length == 0) {
		$input.addClass("is-invalid");
		if (focusIfInvalid) {
			$input.focus();
		}
		console.error("Input value is null or empty for input: " + inputId);
		throw new Error("Input value is null or empty");
	}
	return inputVal.trim();
}


function getIngredientInputValues(e) {
	// data object to store key-value pairs.
	var myData = {}; 
	var areAllInputsValid = true;

	myData["recipeId"] = $("input#id").val();
	
	// Id is unavailable for new ingredients, but for updated ones it shall have a value. 
	myData["id"] = e.currentTarget.dataset.id;
	
	try {
		myData["amount"] = getInputValue("input#input-ingredient-amount");
	} catch (e) {
		areAllInputsValid = false;
	}

	try {
		myData["uom"] = getInputValue("select#input-ingredient-uom");
	} catch (e) {
		areAllInputsValid = false;
	}
	
	try {
		myData["description"] = getInputValue("input#input-ingredient-description");
	} catch (e) {
		areAllInputsValid = false;
	}
	
	return (areAllInputsValid) ? myData : null;
}


function createIngredient(responseData) {
	const $templateDiv = $("#ingredient-template");
	const $clonedDiv = $templateDiv.clone();
	$clonedDiv.attr("id", "ingredient-" + responseData.id);
	const $clonedSpan = $clonedDiv.find("span")
	$clonedSpan.text(responseData.alltext);
	$clonedSpan.attr("data-id", responseData.id);
	$clonedSpan.attr("data-amount", responseData.amount);
	$clonedSpan.attr("data-uomid", responseData.uomid);
	$clonedSpan.attr("data-description", responseData.description);
	
	const $clonedAforRemove = $clonedDiv.find("a.btn-delete-ingredient");
	const newHref = $clonedAforRemove.attr("href").replace("{ingredient.id}", responseData.id);
	$clonedAforRemove.attr("href", newHref);
	$clonedAforRemove.attr("data-id", responseData.id);
	$clonedAforRemove.click(deleteIngredient);
	
	const $clonedAforEdit = $clonedDiv.find("a.btn-edit-ingredient");
	const newHref2 = $clonedAforEdit.attr("href").replace("{ingredient.id}", responseData.id);
	$clonedAforEdit.attr("href", newHref2);
	$clonedAforEdit.attr("data-id", responseData.id);
	$clonedAforEdit.click(startEditingIngredient);
	
	$clonedDiv.insertBefore("div#ingredient-input-box");
	$clonedDiv.removeClass("d-none");
};


function startEditingIngredient(e) {	
	e.preventDefault();
	e.stopPropagation();
	
	$("div.ingredient").removeClass("highlight");
	
	const $sourceRow = $(e.currentTarget.offsetParent);
	$sourceRow.addClass("highlight");
	const $sourceSpan = $sourceRow.find("span");
	
	const $inputBox = $("div#ingredient-input-box");
	$inputBox.addClass("highlight");
	// Find the hidden "amount" field in the selected row and assign its value 
	// to the "amount" field in the input box. Do the same for "uom" and "description" as well.
	$inputBox.find("input#input-ingredient-amount").val($sourceSpan.attr("data-amount"));
	$inputBox.find("select#input-ingredient-uom").val($sourceSpan.attr("data-uomid"));
	$inputBox.find("input#input-ingredient-description").val($sourceSpan.attr("data-description"));
	
	$inputBox.find("a#btn-add-ingredient").addClass("d-none"); // should be invisible.
	
	const $updateBtn = $inputBox.find("a#btn-update-ingredient");
	$updateBtn.removeClass("d-none"); // should be visible.
	$updateBtn.attr("href", e.currentTarget.href);
	$updateBtn.attr("data-id", $sourceSpan.attr("data-id"));
	
	$inputBox.find("a#btn-cancel-update-ingredient").removeClass("d-none"); // should be visible.
};


function updateIngredient(responseData) {
	const $targetSpan = $("div#ingredient-" + responseData.id + " > span");
	//$targetSpan.attr("data-id", responseData.id);
	$targetSpan.attr("data-amount", responseData.amount);
	$targetSpan.attr("data-uomid", responseData.uomid);
	$targetSpan.attr("data-description", responseData.description);
	$targetSpan.text(responseData.alltext);
};

function deleteIngredient(e) {
	e.preventDefault();
	e.stopPropagation();
	
    postData(e.currentTarget.href)
    .then(
		(responseData) => {
			// JSON data parsed by "response.json()" call
			console.log(responseData); 
			$("div#ingredient-" + responseData.id).remove();
			$("a#btn-cancel-update-ingredient").click();
		}, 
		(error) => {
			console.error(error); 
		}
    );
   
};





// https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
// Example POST method implementation using fetch() method.
async function postData(url = "", data = {}) {
	console.log("post-data: {}", data);
  // Default options are marked with *
  const response = await fetch(url, {
    method: "POST", // *GET, POST, PUT, DELETE, etc.
    mode: "cors", // no-cors, *cors, same-origin
    cache: "no-cache", // *default, no-cache, reload, force-cache,
						// only-if-cached
    credentials: "same-origin", // include, *same-origin, omit
    headers: {
      "Content-Type": "application/json"
      // "Content-Type": "application/x-www-form-urlencoded",
    },
    redirect: "follow", // manual, *follow, error
    referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade,
									// origin, origin-when-cross-origin,
									// same-origin, strict-origin,
									// strict-origin-when-cross-origin,
									// unsafe-url
    body: JSON.stringify(data) // body data type must match "Content-Type"
								// header
  });
  
  if (!response.ok) {
      throw new Error('Network response was not ok');
  }
  
  return response.json(); // parses JSON response into native JavaScript
							// objects
}


