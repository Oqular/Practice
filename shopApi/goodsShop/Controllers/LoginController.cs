using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using goodsShop.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;

namespace goodsShop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private readonly GoodsContext _context;

        public LoginController(GoodsContext context)
        {
            _context = context;
        }


        [HttpPost]
        public ActionResult<String> Login(Login login)
        {
            var user = _context.user.Where(u => u.username == login.username && u.password == login.password).FirstOrDefault();
            if(user == null)
            {
                return NotFound();
            }

            return getToken(user);
        }
       
        private ActionResult getToken(User user)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            var tokenDescriptor = new SecurityTokenDescriptor {
                Subject = new ClaimsIdentity(new Claim[]{
                    new Claim(ClaimTypes.Name, user.id.ToString()),
                    new Claim(ClaimTypes.GivenName, user.name),
                    new Claim(ClaimTypes.Role, user.role)
                }),
                Expires = DateTime.Now.AddDays(7),
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };
            var token = tokenHandler.CreateToken(tokenDescriptor);
            user.token = tokenHandler.WriteToken(token);

            return Ok(user);
        }
    }
}