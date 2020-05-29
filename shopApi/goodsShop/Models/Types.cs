using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class Types
    {
        public long id { get; set; }
        public string name { get; set; }
        public List<Goods_Type> goods { get; set; }
    }
}
