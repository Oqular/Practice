using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class Goods
    {
        public long id { get; set; }
        public string title { get; set; }
        public string description { get; set; }
        public string phone { get; set; }
        public string address { get; set; }
        public string type { get; set; } //type of goods?? maybe needs list and pre-set data set
        public List<Image> images { get; set; } //list of byte arrays, one image - one byte array
    }
}
