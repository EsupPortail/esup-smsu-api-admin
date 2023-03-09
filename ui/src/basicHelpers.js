// reify a promise: create a promise and return an object with promise + resolve/reject functions
export const promise_defer = function() {
    let deferred = {}
    deferred.promise = new Promise((resolve, reject) => { deferred.resolve = resolve; deferred.reject = reject });
    return deferred;
};
export const cloneDeep = function (o) {
    return JSON.parse(JSON.stringify(o))
};
export const simpleEach = function (o, f) {
    for (const k in o) {
        f(o[k], k)
    }
};
export const simpleMap = function (o, f) {
    let r = {}
    for (const k in o) {
        r[k] = f(o[k], k)
    }
    return r
};
export const objectSlice = function (o, fields) {
    var r = {};
    for (const field of fields) {
	if (field in o)
	    r[field] = o[field];
    }
    return r;
};
export const array2hash = function (array, field) {
    var h = {};
    for (const e of array) {
	h[e[field]] = e;
    }
    return h;
};
/**
 * 
 * @param {any[]} array 
 * @param {string} field 
 * @returns {Object.<string, any>}
 */
export const array2hashMulti = function (array, field) {
    var h = {};
    for (const e of array) {
	var k = e[field];
	(h[k] = h[k] || []).push(e);
    }
    return h;
};
export const uniqWith = function (array, f) {
    var o = {};
    for (const e of array) {
	var k = f(e);
	if (!(k in o)) o[k] = e;
    }
    return Object.values(o);
};

export const fromJsonOrNull = function(json) {
    try {
	return JSON.parse(json);
    } catch (e) {
	return null;
    }
};

function toCSV(rows) {
    return rows.map(function (row) {
	return row.map(function (v) { 
	    return v.replace(/,/g, '');
	}).join(',');
    }).join("\n");
}

export function exportCSV(domElt, rows, fileName) {
    var csv = toCSV(rows);
    var uri = "data:text/csv;charset=utf-8," + csv;
    var link = document.createElement("a");
    link.setAttribute("href", encodeURI(uri));
    if (fileName) link.setAttribute("download", fileName);
    domElt.appendChild(link); // needed on Firefox, but not Chromium.
    link.click();
};

export function padStart(value, length, char) {
    value = value + '';
    var len = length - value.length;

    if (len <= 0) {
            return value;
    } else {
            return Array(len + 1).join(char) + value;
    }
}

export function formatDate(date, format) {
    const date_ = typeof date === "string" ? new Date(date) : date;
    if (!date) return null;
    return format.split(/(yyyy|MM|dd|HH|mm|ss)/).map(function (item) {
        switch (item) {
            case 'yyyy': return date_.getFullYear();
            case 'MM': return padStart(date_.getMonth() + 1, 2, '0');
            case 'dd': return padStart(date_.getDate(), 2, '0');
            case 'HH': return padStart(date_.getHours(), 2, '0');
            case 'mm': return padStart(date_.getMinutes(), 2, '0');
            case 'ss': return padStart(date_.getSeconds(), 2, '0');
            default: return item;
        }
    }).join('');   
}
