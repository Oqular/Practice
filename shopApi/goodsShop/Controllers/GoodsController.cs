using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using goodsShop.Models;
using System.IO;
using Microsoft.AspNetCore.Http.Internal;
using BrunoZell.ModelBinding;
using Microsoft.AspNetCore.Authorization;

namespace goodsShop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class GoodsController : ControllerBase
    {
        private readonly GoodsContext _context;

        public GoodsController(GoodsContext context)
        {
            _context = context;
        }

        // GET: api/Goods
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Goods>>> Getgoods()
        {
            return await _context.goods.Include(g => g.images).ToListAsync();
        }

        // GET: api/Goods/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetGoods([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var goods = await _context.goods.Include(g => g.images).Include(g => g.type).FirstOrDefaultAsync(i => i.id == id);

            if (goods == null)
            {
                return NotFound();
            }

            return Ok(goods);
        }

        // PUT: api/Goods/5
        [Authorize]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutGoods([FromRoute] int id, [FromBody] Goods goods)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != goods.id)
            {
                return BadRequest();
            }

            List<Image> images = _context.image.Where(i => i.goodsId == id).ToList();
            foreach (var img in images)
            {
                _context.image.Remove(img);
            }
            foreach (var img in goods.images)
            {
                _context.image.Add(img);
            }

            _context.Entry(goods).State = EntityState.Modified;
            
            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GoodsExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }


        // POST: api/Goods
        [Authorize]
        [HttpPost]
        public async Task<IActionResult> PostGoods([ModelBinder(BinderType = typeof(JsonModelBinder))] Goods goods, List<IFormFile> images)//List<IFormFile> images
        {
            //-------------------------------------------------------------------------------
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                _context.goods.Add(goods);

                if (images != null)
                {
                    foreach (var img in images)
                    {
                        Image item = new Image();
                        if (img.Length > 0)
                        {
                            using (var stream = new MemoryStream())
                            {
                                await img.CopyToAsync(stream);
                                item.image = stream.ToArray();
                                item.goodsId = goods.id;
                                _context.image.Add(item);
                            }
                        }
                    }
                }
                
                await _context.SaveChangesAsync();

                return CreatedAtAction("GetGoods", new { id = goods.id }, goods);
            }
            catch (Exception)
            {
                return NoContent();

            }
        }

        // DELETE: api/Goods/5
        [Authorize]
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteGoods([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var goods = await _context.goods.FirstOrDefaultAsync(i => i.id == id);
            List<Image> images = _context.image.Where(i => i.goodsId == id).ToList();
            if (goods == null)
            {
                return NotFound();
            }

            _context.goods.Remove(goods);
            foreach (var img in images) {
                _context.image.Remove(img);
            }
            await _context.SaveChangesAsync();

            return Ok(goods);
        }

        private bool GoodsExists(int id)
        {
            return _context.goods.Any(e => e.id == id);
        }
    }
}