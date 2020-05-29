using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class SpecificType
    {
        public long id { get; set; }
        public string name { get; set; }
        public List<Goods> specGoods { get; set; }
    }
}
