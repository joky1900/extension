Util = {
    setCookie: function (cname, cvalue) {
        let d = new Date();
        d.setTime(d.getTime() + (3*24*60*60*1000)); //valid for 3 days
        let expires = "expires="+ d.toUTCString();

        document.cookie = cname + "=" + cvalue + ";" + expires + "; path=/; SameSite=None; Secure";
    },

    getCookie: function (cname) {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for(let i = 0; i <ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return null;
    },

    randomIntFromInterval: function (min, max) {
        // min and max included
        return Math.floor(Math.random() * (max - min + 1) + min);
    },

    euclideanDistance: function (x1, y1, x2, y2) {
        return Util.roundToTwo(Math.sqrt(Math.pow(Math.abs(x2-x1), 2) + Math.pow(Math.abs(y2-y1), 2)));
    },

    roundToTwo: function(num) {
        return +(Math.round(num + "e+2")  + "e-2");
    },

    shuffle: function(array) {
    let currentIndex = array.length,  randomIndex;

    // While there remain elements to shuffle...
    while (0 !== currentIndex) {

        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex--;

        // And swap it with the current element.
        [array[currentIndex], array[randomIndex]] = [
            array[randomIndex], array[currentIndex]];
    }

        return array;
    }
};